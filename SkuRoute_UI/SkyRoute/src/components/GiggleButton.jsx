import React from "react";
import { motion } from "framer-motion";
import { HelpCircle } from "lucide-react";

export default function GiggleButton({ openDemo }) {
  return (
    <motion.div
      className="fixed bottom-6 right-6 z-[900]" // higher z-index to always float on top
      initial={{ scale: 1 }}
      animate={{
        rotate: [0, -10, 10, -10, 10, 0],
        scale: [1, 1.05, 1],
      }}
      transition={{
        repeat: Infinity,
        repeatDelay: 3, // every 3s
        duration: 0.8,
      }}
    >
      <motion.button
        onClick={openDemo}
        whileHover={{ scale: 1.1 }}
        className="relative group bg-gradient-to-r from-blue-500 to-indigo-600 
                   text-white p-4 rounded-full shadow-2xl hover:shadow-blue-400/40 
                   focus:outline-none"
      >
        {/* Glow ring */}
        <span className="absolute inset-0 rounded-full bg-blue-500 opacity-30 blur-xl animate-ping"></span>

        {/* Icon */}
        <HelpCircle size={28} className="relative z-10" />

        {/* Tooltip on hover */}
        <span className="absolute right-16 top-1/2 -translate-y-1/2 
                         bg-gray-800 text-white text-sm px-3 py-1 
                         rounded-lg opacity-0 group-hover:opacity-100 
                         transition-all">
          Need a Demo?
        </span>
      </motion.button>
    </motion.div>
  );
}
