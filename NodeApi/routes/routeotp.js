const express = require("express");
const router = express.Router();
var ObjectId = require("mongodb").ObjectID;

router.post("/", (req, res) => {
  var msg = "Transaction Successful.";
  dbo
    .collection("transactions")
    .find({
      merchantId: req.body.purchaseInfo.merchantId,
      mobilenumber: req.body.mobile,
      otp: req.body.otp
    })
    .toArray(function(err, found) {
      if (found.length > 0) {
        dbo.collection("transactions").updateOne(
          {
            mobilenumber: req.body.mobile,
            merchantId: req.body.purchaseInfo.merchantId,
            otp: req.body.otp
          },
          { $set: { message: msg, modifiedTime: new Date() } },
          function(err, updated) {
            dbo
              .collection("transactions")
              .find({
                mobilenumber: req.body.mobile,
                merchantId: req.body.purchaseInfo.merchantId
              })
              .toArray(function(err, result) {
                if (result.length > 0) {
                  var output = {
                    message: result[0].message,
                    receipt: {
                      transactionAmount: {
                        amount: result[0].transactionamount,
                        currency: "INR",
                        transactionId: result[0].transactionId
                      }
                    },
                    status: result[0].status
                  };
                  res.send(output);
                } //if
              });
          }
        );
      } //if
      else {
        var output1 = {
          message: "Please Enter Correct OTP",
          receipt: {
            transactionAmount: {
              amount: "",
              currency: "INR"
            }
          },
          status: "Failed"
        };
        res.send(output1);
      }
    });
});

module.exports = router;
