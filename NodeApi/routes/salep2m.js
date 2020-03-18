const express = require("express");
const router = express.Router();
const Transations = require("../models/transactions");

router.post("/", async (req, res) => {
  var transactionamount = req.body.amount,
    mobilenumber = req.body.mobile,
    merchantId = req.body.purchaseInfo.merchantId;
  var message = "Payment request initiated successfully";
  var status = "SUCCESS";
  var otp = Math.floor(1000 + Math.random() * 9000);

  const newTransaction = new Transations({
    transactionamount,
    mobilenumber,
    message,
    status,
    merchantId,
    otp
  });
  const transaction = await newTransaction.save();
  var resp = {
    message: transaction.message,
    receipt: {
      transactionAmount: {
        amount: transaction.transactionamount,
        currency: "INR"
      },
      transactionId: transaction._id
    },
    status: transaction.status
  };

  res.json(resp);
});

module.exports = router;
