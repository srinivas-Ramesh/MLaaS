Format of JSON file to be sent.

{
 attributes: {1,2,5...},
 type:"String/numeric"
 dataArray:{
 {1,2,3},
 {4,5,6},
 ..
 ..
 ..
 }
 }
 
 
 example
 ----------
 
 {
 "attributes":["att1","att2","att3"],
 "type":"String",
 "data":[
   ["a","b","c"],
   ["d","e","f"]
   ]
}