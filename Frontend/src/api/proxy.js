import axios from "axios"

export default handler=async(req,res)=>{
    try{
        const response = await fetch("http://65.1.248.66:8080",{
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(req.body) 
            
        })
        const data = await response.json();
        res.status(200).json(data);
    }catch(error){
        res.status(500).json({ error: error?.response?.data?.message });
    }
}