const express = require("express");
const router = express.Router();
const Users = require("../models/users");
const Merchants = require("../models/merchant");
const Terminallist = require("../models/terminallist");

router.post("/", (req, res) => {
  dbo
    .collection("users")
    .find({ userId: req.body.userId, password: req.body.password })
    .toArray(function(err, result1) {
      if (err) {
        var out = {
          merchantInfo: null,
          terminalList: null
        };
        res.json(out);
      }

      if (result1.length) {
        dbo
          .collection("merchants")
          .find({ merchantId: req.body.userId })
          .toArray(function(err, result2) {
            dbo
              .collection("terminallists")
              .find({ userId: req.body.userId })
              .toArray(function(err, result3) {
                var output = {
                  merchantInfo: result2[0],
                  terminalList: result3
                };
                res.json(output);
              });
          });
      } else {
        
        var out = {
          merchantInfo: null,
          terminalList: null
        };
        res.json(out);
      }
    });
});

module.exports = router;
