import { motion } from "framer-motion";
import cloud from "../assets/cloud2.png";
import airplane from "../assets/airplane3.png";
import bird from "../assets/bird2.gif";
import { useNavigate } from "react-router-dom";
import AboutUsModal from "./AboutUsModal";
import { useState } from "react";

export default function SplashScreenMobile() {
    const [isOpen, setIsOpen] = useState(false);
    const navigate = useNavigate();

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gradient-to-b from-sky-200 via-sky-300 to-sky-500 px-4 py-8 relative overflow-hidden text-center lg:hidden">

            {/* Sun */}
            <motion.div
                className="absolute top-6 right-6 w-20 h-20 rounded-full 
        bg-gradient-to-t from-orange-200 via-yellow-300 to-yellow-100 
        shadow-[0_0_50px_20px_rgba(255,223,100,0.6)] z-0"
                animate={{
                    rotate: 360,
                    opacity: [0.85, 1, 0.85],
                    scale: [1, 1.07, 1]
                }}
                transition={{
                    rotate: { duration: 50, repeat: Infinity, ease: "linear" },
                    opacity: { duration: 6, repeat: Infinity, ease: "easeInOut" },
                    scale: { duration: 6, repeat: Infinity, ease: "easeInOut" }
                }}
            />

            {/* Text Section */}
            <div className="max-w-md z-10">
                <p className="uppercase text-yellow-400 font-semibold drop-shadow-md text-sm sm:text-base">
                    ✦ Smarter Connections
                </p>

                <h1 className="mt-4 text-3xl sm:text-4xl font-extrabold leading-snug text-white drop-shadow-lg">
                    Discover the Shortest <br /> Flight Routes
                </h1>

                <p className="mt-4 text-base sm:text-lg text-gray-50 drop-shadow-sm">
                    Plan your journey with ease. From any source to destination,
                    our system identifies the most efficient path with clear mapping and seamless visualization.
                </p>

                <div className="mt-6 flex flex-col sm:flex-row justify-center gap-3">
                    <button
                        className="px-6 py-3 sm:px-8 sm:py-4 bg-yellow-400 text-gray-900 font-semibold rounded-lg shadow-lg hover:bg-yellow-500 transition"
                        onClick={() => navigate("/path")}
                    >
                        Find Routes →
                    </button>
                    <AboutUsModal isOpen={isOpen} setIsOpen={setIsOpen} />
                </div>
            </div>

            {/* Airplane */}
            <motion.img
                src={airplane}
                alt="Airplane"
                className="w-64 sm:w-80 mt-8 z-20"
                initial={{ x: -150, y: -150, rotate: -30, scale: 0.8, opacity: 0 }}
                animate={
                    isOpen
                        ? {
                            x: -970,
                            y: 620,
                            opacity: 1,
                            rotate: -5,
                            scale: 1,
                            transition: { duration: 2, ease: "easeInOut" }
                        }
                        : {
                            x: -20,
                            y: 20,
                            opacity: 1,
                            rotate: -5,
                            scale: 1,
                            transition: { duration: 2, ease: "easeInOut" }
                        }
                }
                transition={{ duration: 4, ease: "easeInOut" }}
            />

            {/* Clouds */}
            <motion.img
                src={cloud}
                alt="Cloud"
                className="absolute top-10 left-2 w-28 opacity-50"
                animate={{ x: [0, 30, 0] }}
                transition={{ duration: 18, repeat: Infinity, ease: "linear" }}
            />
            <motion.img
                src={cloud}
                alt="Cloud"
                className="absolute top-24 right-4 w-36 opacity-60"
                animate={{ x: [0, -40, 0] }}
                transition={{ duration: 20, repeat: Infinity, ease: "linear" }}
            />
            <motion.img
                src={cloud}
                alt="Cloud"
                className="absolute bottom-16 left-4 w-32 opacity-40"
                animate={{ x: [0, 25, 0] }}
                transition={{ duration: 22, repeat: Infinity, ease: "linear" }}
            />

            {/* Bird */}
            <motion.img
                src={bird}
                alt="Bird"
                className="absolute top-32 w-20 opacity-90"
                initial={{ x: -100 }}
                animate={{ x: "110vw" }}
                transition={{ duration: 18, repeat: Infinity, ease: "linear" }}
            />

        </div>
    );
}
