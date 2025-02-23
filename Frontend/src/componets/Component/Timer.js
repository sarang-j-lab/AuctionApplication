import { useState, useEffect } from "react";

const Timer = ({initialTime}) => {
  const [time, setTime] = useState(initialTime);

  useEffect(() => {
    if (time <= 0) return;

    const timer = setInterval(() => {
      setTime((prevTime) => prevTime - 1);
    }, 1000);

    return () => clearInterval(timer);
  }, [time]);

  return (
    <div>
      <h1 className="text-black text-[100px] font-serif"> {time} </h1>
    </div>
  );
};

export default Timer;
