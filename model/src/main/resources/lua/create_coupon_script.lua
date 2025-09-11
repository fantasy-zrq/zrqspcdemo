redis.call('HMSET', KEYS[1], unpack(ARGV, 1, #ARGV - 1))
redis.call('EXPIREAT', KEYS[1], ARGV[#ARGV])