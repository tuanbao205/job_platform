import { Redis as UpstashRedis } from "@upstash/redis";
import { createClient } from "redis";

type RedisStore = {
  get: <T = unknown>(key: string) => Promise<T | null>;
  set: (key: string, value: string, options?: { ex?: number }) => Promise<unknown>;
  del: (...keys: string[]) => Promise<number>;
  ttl: (key: string) => Promise<number>;
  ping: () => Promise<string>;
};

let redis: RedisStore | null = null;
let upstashRedis: UpstashRedis | null = null;

const getUpstashConfig = () => {
  const url = process.env.UPSTASH_REDIS_REST_URL?.trim() || "";
  const token = process.env.UPSTASH_REDIS_REST_TOKEN?.trim() || "";
  const hasPlaceholder = /your-|placeholder|example|chèn/i.test(`${url} ${token}`);

  return url && token && !hasPlaceholder ? { url, token } : null;
};

export const isUsingLocalRedis = () =>
  !getUpstashConfig() && process.env.NODE_ENV !== "production";

export const getUpstashRedis = () => {
  const config = getUpstashConfig();

  if (!config) {
    throw new Error("Valid Upstash Redis credentials are not configured.");
  }

  upstashRedis ??= new UpstashRedis(config);
  return upstashRedis;
};

const createUpstashStore = (client: UpstashRedis): RedisStore => {
  return {
    get: <T>(key: string) => client.get<T>(key),
    set: (key, value, options) =>
      options?.ex
        ? client.set(key, value, { ex: options.ex })
        : client.set(key, value),
    del: (...keys) => client.del(...keys),
    ttl: (key) => client.ttl(key),
    ping: () => client.ping(),
  };
};

const createLocalStore = (): RedisStore => {
  const client = createClient({
    url: process.env.REDIS_URL?.trim() || "redis://localhost:6379",
  });
  client.on("error", (error) => console.error("Local Redis connection error:", error));
  const connected = client.connect();

  return {
    get: async <T>(key: string) => {
      await connected;
      return (await client.get(key)) as T | null;
    },
    set: async (key, value, options) => {
      await connected;
      return options?.ex
        ? client.set(key, value, { EX: options.ex })
        : client.set(key, value);
    },
    del: async (...keys) => {
      await connected;
      return keys.length ? client.del(keys) : 0;
    },
    ttl: async (key) => {
      await connected;
      return client.ttl(key);
    },
    ping: async () => {
      await connected;
      return client.ping();
    },
  };
};

export const getRedis = (): RedisStore => {
  if (redis) {
    return redis;
  }

  const upstash = getUpstashConfig();

  if (upstash) {
    redis = createUpstashStore(getUpstashRedis());
    return redis;
  }

  if (process.env.NODE_ENV === "production") {
    throw new Error(
      "Set UPSTASH_REDIS_REST_URL and UPSTASH_REDIS_REST_TOKEN for production.",
    );
  }

  redis = createLocalStore();
  return redis;
};

export const pingRedis = async () => {
  const result = await getRedis().ping();

  if (result !== "PONG") {
    throw new Error(`Redis ping failed: ${String(result)}`);
  }

  return result;
};
