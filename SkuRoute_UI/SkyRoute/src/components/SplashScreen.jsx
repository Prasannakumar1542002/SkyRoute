import { motion } from "framer-motion";
import cloud from "../assets/cloud2.png";
import airplane from "../assets/airplane3.png";
import bird from "../assets/bird2.gif";
import { useNavigate } from "react-router-dom";
import AboutUsModal from "./AboutUsModal";
import { useState } from "react";

export default function SplashScreen() {
    const [isOpen, setIsOpen] = useState(false);
    const navigate = useNavigate();
    return (
        <div className="min-h-screen bg-gradient-to-b from-sky-200 via-sky-300 to-sky-500 flex flex-col">
            {/* Navbar
            <nav className="flex justify-between items-center px-12 py-6 bg-transparent backdrop-blur-md">
                <div className="text-xl font-bold text-white">✈️ SkyWay</div>
                <ul className="flex space-x-8 text-white font-medium">
                    <li className="hover:text-yellow-300 cursor-pointer">Home</li>
                    <li className="hover:text-yellow-300 cursor-pointer">About</li>
                    <li className="hover:text-yellow-300 cursor-pointer">Features</li>
                    <li className="hover:text-yellow-300 cursor-pointer">Contact</li>
                </ul>
            </nav> */}

            {/* Hero Section */}
            <div className="flex flex-1 items-center justify-between px-20 relative overflow-hidden">
                {/* Left Text */}
                <div className="max-w-xl z-10">
                    <p className="uppercase text-yellow-400 font-semibold drop-shadow-md">
                        ✦ Smarter Connections
                    </p>

                    <h1 className="mt-4 text-5xl font-extrabold leading-tight text-white drop-shadow-lg">
                        Discover the Shortest <br /> Flight Routes
                    </h1>

                    <p className="mt-4 text-lg text-gray-50 drop-shadow-sm">
                        Plan your journey with ease. From any source to destination,
                        our system identifies the most efficient path with clear
                        mapping and seamless visualization.
                    </p>

                    <button
                        className="mt-6 mr-2 px-8 py-4 bg-yellow-400 text-gray-900 font-semibold rounded-lg shadow-lg hover:bg-yellow-500 transition"
                        onClick={() => navigate("/path")}
                    >
                        Find Routes →
                    </button>
                    <AboutUsModal isOpen={isOpen} setIsOpen={setIsOpen} />
                </div>



                {/* Airplane */}
                <motion.img
                    src={airplane}
                    alt="Airplane"
                    className="w-[600px] z-20"
                    initial={{ x: 400, y: -200, opacity: 0, rotate: 10 }}
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
                                x: -370,
                                y: 120,
                                opacity: 1,
                                rotate: -5,
                                scale: 1,
                                transition: { duration: 2, ease: "easeInOut" }
                            }
                    }
                    transition={{ duration: 4, ease: "easeInOut" }}
                />

                {/* Sun */}
                <motion.div
                    className="absolute top-10 right-32 w-40 h-40 rounded-full 
             bg-gradient-to-t from-orange-200 via-yellow-300 to-yellow-100 
             shadow-[0_0_100px_40px_rgba(255,223,100,0.6)] z-0"
                    animate={{
                        rotate: 360,
                        opacity: [0.85, 1, 0.85], // soft fade
                        scale: [1, 1.07, 1]       // gentle pulsing
                    }}
                    transition={{
                        rotate: { duration: 50, repeat: Infinity, ease: "linear" },
                        opacity: { duration: 6, repeat: Infinity, ease: "easeInOut" },
                        scale: { duration: 6, repeat: Infinity, ease: "easeInOut" }
                    }}
                />


                {/* Clouds (layered & denser) */}
                <motion.img
                    src={cloud}
                    alt="Cloud"
                    className="absolute top-20 right-10 w-[400px] opacity-70"
                    animate={{ x: [0, -60, 0] }}
                    transition={{ duration: 18, repeat: Infinity, ease: "linear" }}
                />
                <motion.img
                    src={cloud}
                    alt="Cloud"
                    className="absolute bottom-10 left-0 w-[500px] opacity-50"
                    animate={{ x: [0, 70, 0] }}
                    transition={{ duration: 25, repeat: Infinity, ease: "linear" }}
                />
                <motion.img
                    src={cloud}
                    alt="Cloud"
                    className="absolute top-40 left-20 w-[300px] opacity-60"
                    animate={{ x: [0, 40, 0] }}
                    transition={{ duration: 20, repeat: Infinity, ease: "linear" }}
                />
                <motion.img
                    src={cloud}
                    alt="Cloud"
                    className="absolute bottom-32 right-40 w-[350px] opacity-40"
                    animate={{ x: [0, -30, 0] }}
                    transition={{ duration: 22, repeat: Infinity, ease: "linear" }}
                />
                <motion.img
                    src={cloud}
                    alt="Cloud"
                    className="absolute top-10 left-1/2 w-[250px] opacity-50"
                    animate={{ x: [0, -50, 0] }}
                    transition={{ duration: 28, repeat: Infinity, ease: "linear" }}
                />
                <motion.img
                    src={bird}
                    alt="Bird"
                    className="absolute top-32 w-[120px] opacity-90"
                    initial={{ x: -200, y: 10 }}
                    animate={{ x: "110vw", y: 10 }}
                    transition={{ duration: 18, repeat: Infinity, ease: "linear" }}
                />
            </div>
        </div>
    );
}
