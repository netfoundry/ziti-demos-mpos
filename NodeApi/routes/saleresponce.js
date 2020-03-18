const express = require("express");
const router = express.Router();



router.post('/',(req,res) => {

    res.send("{\"message\":\"Payment request initiated successfully.\",\"receipt\":{\"transactionAmount\":{\"amount\":\"2.58\",\"currency\":\"INR\"},\"transactionId\":\"1021571895574634000\"},\"status\":\"SUCCESS\"}");


});
module.exports = router;