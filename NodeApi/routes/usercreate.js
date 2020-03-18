const express = require("express");
const router = express.Router();
const users= require('../models/users');

router.post('/',async(req,res) =>{

const{username,password}=req.body;
var userid ="enlume1";
var merchantid ="m001";

const usercreation = new users({
   username,password,userid,merchantid
  });

  const user = await usercreation.save();

  res.json(user);

});

module.exports=router;