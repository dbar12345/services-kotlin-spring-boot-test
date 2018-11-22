package com.urbn.rewards.service

import com.google.gson.Gson
import com.urbn.rewards.models.Customer
import com.urbn.rewards.models.OrderRequest
import com.urbn.rewards.models.Rewards
import org.springframework.stereotype.Service

@Service
class OrderService {

    // Hard coded list of rewards. See the getRewards function below
    final var rewards: Array<Rewards>

    // A list of in memory customers keyed off of the e-mail
    private val customers = HashMap<String, Customer>()

    private val gson = Gson()

    // Stores reward data into rewards array
    init {
        val rewardString = getRewards()
        rewards = gson.fromJson(rewardString, Array<Rewards>::class.java)
    }

    fun purchase(orderRequest: OrderRequest): Customer {
        // Implement purchase endpoint logic here
        // Right now, we're only storing the customer into a hash map

        val currentCustomer = Customer(
            email = orderRequest.email,
            rewardPoints = Math.floor(orderRequest.purchaseTotal.toDouble()).toInt(),
            nextRewardsTier = "??",
            rewardsTier = "???",
            rewardsTierName = "???",
            nextRewardsTierName = "???",
            nextRewardsTierProgress = 0.0.toFloat()
        )

        customers[orderRequest.email] = currentCustomer

        return currentCustomer
    }

    private fun getRewards(): String {
        return """
            [
                { "tier": "A", "rewardName": "5% off purchase", "points": 100 },
                { "tier": "B", "rewardName": "10% off purchase", "points": 200 },
                { "tier": "C", "rewardName": "15% off purchase", "points": 300 },
                { "tier": "D", "rewardName": "20% off purchase", "points": 400 },
                { "tier": "E", "rewardName": "25% off purchase", "points": 500 },
                { "tier": "F", "rewardName": "30% off purchase", "points": 600 },
                { "tier": "G", "rewardName": "35% off purchase", "points": 700 },
                { "tier": "H", "rewardName": "40% off purchase", "points": 800 },
                { "tier": "I", "rewardName": "45% off purchase", "points": 900 },
                { "tier": "J", "rewardName": "50% off purchase", "points": 1000 }
            ]
            """.trimIndent()
    }

    // Implement reward endpoint logic here
    fun reward(email: String): Rewards {

        var customerReward = Rewards()
        val customer = customers[email]

        if (customer != null) {
            customerReward = Rewards(
                    rewardName = customer.nextRewardsTier,
                    tier = customer.rewardsTier,
                    points = customer.rewardPoints
            )
        }

        return customerReward
    }

    // Implement allRewards endpoint logic here
    fun allRewards(): HashMap<String, Rewards> {
        val customersRewards = HashMap<String, Rewards>()

        for ((key, value) in customers) {
            val rewards = Rewards(
                    rewardName = value.rewardsTierName,
                    tier = value.rewardsTier,
                    points = value.rewardPoints
            )
            customersRewards[key] = rewards
        }

        return customersRewards
    }

}
