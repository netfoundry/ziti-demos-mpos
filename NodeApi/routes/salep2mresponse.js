const express = require("express");
const router = express.Router();
const Merchant = require("../models/merchant");
const Users = require("../models/users");
const Transations = require("../models/transactions");

router.post("/", async (req, res) => {
  for (var i = 0; i < req.body.length; i++) {
    dbo
      .collection("transactions")
      .updateOne(
        { _id: req.body[i] },
        { $set: { status: "Approved" } },
        function(err, updated) {}
      );
  } //for
});

module.exports = router;
