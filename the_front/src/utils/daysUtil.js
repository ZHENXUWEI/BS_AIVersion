
export function daysUntil(targetDateStr) {
    console.log(targetDateStr)
    if(targetDateStr==null) return "长期政策"
    const targetDate = new Date(targetDateStr);
    const currentDate = new Date();
    const diff = targetDate - currentDate;
    const days = diff / (1000 * 60 * 60 * 24);
    if(days<=0){
        return "已过期";
    }else{
        if(Math.floor(days)>=500){
            return "长期"
        }else{
            return "剩余"+Math.floor(days)+"天";
        }
        
    }
   
}