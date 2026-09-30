import dotenv from "dotenv";
import { getRedis, isUsingLocalRedis, pingRedis } from "../config/redis";

dotenv.config();

const run = async () => {
  console.log("Redis test:");
  console.log("  driver:", isUsingLocalRedis() ? "local TCP" : "Upstash REST");

  const pong = await pingRedis();
  console.log("  ping:", pong);

  const redis = getRedis();
  const key = `test-redis:${Date.now()}`;
  await redis.set(key, "ok", { ex: 30 });
  const value = await redis.get(key);
  await redis.del(key);

  if (value !== "ok") {
    throw new Error(`Redis set/get failed. Expected "ok", got ${String(value)}`);
  }

  console.log("  set/get:", value);
  console.log("Redis connection + read/write test passed");
};

void run().catch((error) => {
  console.error("Redis test failed:", error);
  process.exit(1);
});
