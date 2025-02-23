import React from "react";
import { motion } from "framer-motion";
import { X } from "lucide-react";

export const SuccessToast = ({ message, onClose }) => {

    

  return (
    <motion.div
      initial={{ opacity: 0, x: 50 }}
      animate={{ opacity: 1, x: 0 }}
      exit={{ opacity: 0, x: 50 }}
      className="fixed top-4 right-4 z-50 flex items-center gap-4 rounded-2xl bg-blue-500 p-4 text-white shadow-lg"
    >
      <span className="text-sm font-medium">{message}</span>
      <button
        onClick={onClose}
        className="rounded-full p-1 text-white hover:bg-blue-600"
      >
        <X className="h-4 w-4" />
      </button>
    </motion.div>
  );
};
export const ErrorToast = ({ message, onClose }) => {

    

  return (
    <motion.div
      initial={{ opacity: 0, x: 50 }}
      animate={{ opacity: 1, x: 0 }}
      exit={{ opacity: 0, x: 50 }}
      className="fixed top-4 right-4 z-50 flex items-center gap-4 rounded-2xl bg-red-500 p-4 text-white shadow-lg"
    >
      <span className="text-sm font-medium">{message}</span>
      <button
        onClick={onClose}
        className="rounded-full p-1 text-white hover:bg-red-600"
      >
        <X className="h-4 w-4" />
      </button>
    </motion.div>
  );
};




