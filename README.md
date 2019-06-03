# TurvoFlashSale

To Run the application need to start 'SpringBootRestApiApp' or Run jar create for the same.
Below are the available API's listed with required body content and responses :-

API         : http://localhost:8080/TurvoFlashSaleAPI/user/shoppingCartConfirmation    
Method Type : POST
Content-Type: application/json
Body        :

{
"cartLines":[
{
"productInfo":{
  "code":103
},
"quantity": 20
}
]
}



API        :  http://localhost:8080/TurvoFlashSaleAPI/user/order?orderId=8
Mthod Type : GET
Response   :

{
  "id": "b2220c66-4fb2-42ae-93b5-f2d731a34d7f",
  "orderDate": 1559540720000,
  "orderNum": 8,
  "amount": 0.0,
  "customerName": "vikash",
  "customerAddress": "Hyd",
  "customerEmail": "vikash@gmail.com",
  "customerPhone": "12345678",
  "details": [
    {
      "id": "6545c23c-d8b1-439b-a80a-003e75005f9c",
      "productCode": "103",
      "productName": "product1",
      "quanity": 14,
      "price": 101.0,
      "amount": 2020.0
    }
  ]
}

API         : http://localhost:8080/TurvoFlashSaleAPI/user?userName=vikash
Method Type : GET
Response    : 

{
  "userName": "vikash",
  "encrytedPassword": "vikash123",
  "active": true,
  "userRole": "USER",
  "address": "Hyd",
  "email": "vikash@gmail.com",
  "phone": "12345678",
  "role": {
    "id": 1,
    "name": "admin",
    "new": false
  }
}

API         : http://localhost:8080/TurvoFlashSaleAPI/createUser
Method Type : POST
Body        :


{
  "userName": "user1",
  "encrytedPassword": "vikash123",
  "active": true,
  "userRole": "user",
  "address": "Hyd",
  "email": "vikash@gmail.com",
  "phone":"6767676767"
}


API         : http://localhost:8080/TurvoFlashSaleAPI/product?sku=102
Method Type : GET
Response    :

{
  "code": "102",
  "name": "watch2",
  "price": 100.0,
  "image": null,
  "createDate": 1559294636000,
  "saleType": "1",
  "capacity": 10,
  "soldOut": true
}

API          : http://localhost:8080/TurvoFlashSaleAPI/product/productList?saleType=flashsale 
    saleType : will give product listing for particular type of sale.
Method Type: GET
Response     :


{
  "flashSale": true,
  "productInfo": {
    "totalRecords": 1,
    "currentPage": 1,
    "list": [
      {
        "code": "103",
        "name": "product1",
        "price": 101.0,
        "image": null,
        "createDate": 1559325984000,
        "saleType": "flashSale",
        "capacity": 20,
        "soldOut": true
      }
    ],
    "maxResult": 1,
    "totalPages": 1,
    "navigationPages": [
      1,
      1
    ]
  }
}

