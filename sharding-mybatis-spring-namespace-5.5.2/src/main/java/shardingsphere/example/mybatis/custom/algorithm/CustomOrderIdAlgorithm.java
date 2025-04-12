package shardingsphere.example.mybatis.custom.algorithm;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.List;

public class CustomOrderIdAlgorithm implements StandardShardingAlgorithm<Integer> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Integer> shardingValue) {
        //演示自定义分片算法
        //user_id是vip，在t_order_vip表中，否则在t_order_normal表中
        String tableSuffix = "";
        if (isVip(shardingValue.getValue())) {
            tableSuffix = "_vip";
        } else {
            tableSuffix = "_normal";
        }
        for (String each : availableTargetNames) {
            if (each.endsWith(tableSuffix)) {
                return each;
            }
        }
        return "";
    }

    boolean isVip(Integer userId) {
        Integer value = userId % 10;
        //user_id是1，2，3的视为vip(仅为演示)
        List<Integer> vipUserIdList = List.of(1, 2, 3);
        return vipUserIdList.contains(value);
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Integer> shardingValue) {
        throw new RuntimeException("not supported");
    }
}
