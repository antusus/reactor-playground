package contracts.customers

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url "/customers"
    }
    response {
        body(
                """
            [
            { "id": 1, "name" : "Jane" },
            { "id": 2, "name" : "John" }
            ]
        """
        )
        status(200)
        headers {
            contentType(applicationJson())
        }
    }
}