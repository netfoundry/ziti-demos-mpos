const express = require("express");
const router = express.Router();

router.post("/", (req, res) => {
  res.send(
    '{"personalInfo":null,"status":"SUCCESS","message":"Amount of INR 1.23 is approved","receipt":{"transactionTime":"2019-10-21T07:38:00.470+0000","transactionId":1021571643480386000,"transactionAmount":{"amount":1.23,"currency":"INR"},"taxAmount":{"amount":null,"currency":"INR"},"feeAmount":{"amount":null,"currency":"INR"},"merchantName":null,"merchantAddress":null,"responseMessage":"Amount of INR 1.23 is approved","rrn":"OMMK207YGZ7N"},"balance":null,"authCode":"SDX5KK"}'
  );
});

module.exports = router;
