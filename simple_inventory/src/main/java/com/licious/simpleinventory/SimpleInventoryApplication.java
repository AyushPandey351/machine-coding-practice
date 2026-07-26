package com.licious.simpleinventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Develop an Inventory Management System with Deduct/Add Inventory functionality.
// Requirements:
// - RESTful API to handle inventory management operations.
// - Endpoints for deducting and adding inventory quantities.
// - In-memory data structures are fine for this scope.
// - Use concurrent data structures / thread-safe operations so concurrent
//   add/deduct calls for the same product can't race each other.
@SpringBootApplication
public class SimpleInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleInventoryApplication.class, args);
	}

}
