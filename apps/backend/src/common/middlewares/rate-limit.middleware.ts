import { Ratelimit } from "@upstash/ratelimit";
import type { NextFunction, Request, Response } from "express";
import { getUpstashRedis, isUsingLocalRedis } from "../../config/redis";
import { AppError } from "../errors/app-error";

export type RateLimitOptions = {
  /** Dinh danh limiter, dung lam prefix key trong Redis. */
  name: string;
  /** So request toi da trong mot cua so. */
  limit: number;
  /** Do dai cua so, tinh bang giay. */
  windowSeconds: number;
  /** Mac dinh dung req.ip. Tra ve chuoi rong de bo qua limiter cho request do. */
  keyFn?: (req: Request) => string;
};

const getLimitFactor = () => {
  const factor = Number(process.env.RATE_LIMIT_FACTOR);
  return Number.isFinite(factor) && factor >= 1 ? factor : 1;
};

const limiters = new Map<string, Ratelimit>();
const localWindows = new Map<string, number[]>();

// Initialize Upstash lazily so missing credentials do not fail during module import.
const getLimiter = (options: RateLimitOptions) => {
  const cached = limiters.get(options.name);

  if (cached) {
    return cached;
  }

  const limiter = new Ratelimit({
    redis: getUpstashRedis(),
    limiter: Ratelimit.slidingWindow(
      Math.floor(options.limit * getLimitFactor()),
      `${options.windowSeconds} s`
    ),
    prefix: `rl:${options.name}`,
    analytics: false,
  });

  limiters.set(options.name, limiter);
  return limiter;
};

const limitLocally = (options: RateLimitOptions, key: string) => {
  const limit = Math.floor(options.limit * getLimitFactor());
  const windowMs = options.windowSeconds * 1000;
  const now = Date.now();
  const bucketKey = `${options.name}:${key}`;
  const timestamps = (localWindows.get(bucketKey) ?? []).filter(
    (timestamp) => timestamp > now - windowMs,
  );
  const success = timestamps.length < limit;

  if (success) {
    timestamps.push(now);
  }

  if (timestamps.length) {
    localWindows.set(bucketKey, timestamps);
  } else {
    localWindows.delete(bucketKey);
  }

  return {
    success,
    limit,
    remaining: Math.max(limit - timestamps.length, 0),
    reset: (timestamps[0] ?? now) + windowMs,
  };
};

const setHeaders = (
  res: Response,
  result: { limit: number; remaining: number; reset: number },
) => {
  res.setHeader("RateLimit-Limit", String(result.limit));
  res.setHeader("RateLimit-Remaining", String(Math.max(result.remaining, 0)));
  res.setHeader("RateLimit-Reset", String(Math.ceil(result.reset / 1000)));
};

/**
 * Gioi han tan suat theo IP, luu tren Upstash Redis (sliding window).
 * Dat TRUOC validateBody de chan flood truoc khi ton cong parse body.
 */
export const rateLimit = (options: RateLimitOptions) => {
  return async (req: Request, res: Response, next: NextFunction) => {
    const key = options.keyFn ? options.keyFn(req) : req.ip || "unknown";

    if (key.length === 0) {
      next();
      return;
    }

    let result: {
      success: boolean;
      limit: number;
      remaining: number;
      reset: number;
    };

    if (isUsingLocalRedis()) {
      result = limitLocally(options, key);
    } else {
      try {
        result = await getLimiter(options).limit(key);
      } catch (error) {
        // Fail-open: Redis chet khong duoc keo sap toan bo auth.
        console.error(`Rate limit "${options.name}" unavailable:`, error);
        next();
        return;
      }
    }

    setHeaders(res, result);

    if (result.success) {
      next();
      return;
    }

    const retryAfterSeconds = Math.max(Math.ceil((result.reset - Date.now()) / 1000), 1);
    res.setHeader("Retry-After", String(retryAfterSeconds));

    next(
      new AppError(
        429,
        "TOO_MANY_REQUESTS",
        `Bạn thao tác quá nhiều lần, vui lòng thử lại sau ${retryAfterSeconds} giây`,
      ),
    );
  };
};
