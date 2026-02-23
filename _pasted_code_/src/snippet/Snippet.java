package snippet;

public class Snippet {
	spring:
	  application:
	    name: api-gateway
	
	  cloud:
	    gateway:
	      mvc:
	        routes:
	          - id: account-service
	            uri: http://localhost:8081/accounts
	            predicates:
	              - Path=/accounts/**
	
	server:
	  port: 8888
}

