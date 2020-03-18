const express = require("express");
const cors = require("cors");
const router = express.Router();

router.get("/", cors(), (req, res) => {
  dbo
    .collection("merchants")
    .aggregate([
      {
        $lookup: {
          from: "transactions",
          localField: "merchantId",
          foreignField: "merchantId",
          as: "merchantdetails"
        }
      }
    ])
    .toArray(function(err, result) {
      if (err) throw err;
      res.json(result);
    });
});

router.get("/:merchantId", cors(), (req, res) => {
  let id = req.params.id;
  dbo
    .collection("transactions")
    .find({ id })
    .toArray(function(err, result) {
      if (err) throw err;
      console.log(result);
      res.json(result);
    });
});

module.exports = router;
