/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package shardingsphere.example.mybatis.service;

import shardingsphere.example.mybatis.entity.Order;
import shardingsphere.example.mybatis.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public final class ExampleService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleService.class);
    
    private final OrderRepository orderRepository;
    

    
    public ExampleService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;

    }
    
    public void run() throws SQLException {
        try {
            this.initEnvironment();
            this.processSuccess();
        } finally {
            this.cleanEnvironment();
        }
    }
    
    private void initEnvironment() throws SQLException {
        orderRepository.createTableIfNotExists();
        orderRepository.truncateTable();

    }
    
    private void processSuccess() throws SQLException {
        LOGGER.info("-------------- Process Success Begin ---------------");
        List<Integer> userIds = insertData();
        printData(); 
        deleteData(userIds);
        printData();
        LOGGER.info("-------------- Process Success Finish --------------");
    }
    
    private List<Integer> insertData() throws SQLException {
        LOGGER.info("---------------------------- Insert Data ----------------------------");
        List<Integer> result = new ArrayList<>(10);
        for (int i = 1; i <= 5; i++) {
            Order order = new Order();
            order.setUserId(i);
            order.setOrderType(i % 2);
            order.setAddressId(i);
            order.setStatus("INSERT_TEST");
            orderRepository.insert(order);
            result.add(order.getUserId());
        }
        return result;
    }
    
    private void deleteData(final List<Integer> userIds) throws SQLException {
        LOGGER.info("---------------------------- Delete Data ----------------------------");
        for (Integer each : userIds) {
            orderRepository.delete(each);
        }
    }
    
    private void printData() throws SQLException {
        LOGGER.info("---------------------------- Print Order Data -----------------------");
        for (Object each : this.selectAll()) {
            LOGGER.info(each.toString());
        }
    }
    
    private List<Order> selectAll() throws SQLException {
        List<Order> result = orderRepository.selectAll();
        return result;
    }

    
    private void cleanEnvironment() throws SQLException {
        orderRepository.dropTable();

    }
}
