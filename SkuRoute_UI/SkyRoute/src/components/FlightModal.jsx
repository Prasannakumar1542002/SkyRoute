import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { X } from "lucide-react";

import AirIndia from "../assets/Air India.png";
import IndiGo from "../assets/IndiGo.png";
import GoAir from "../assets/GoAir.png";
import AirAsiaIndia from "../assets/AirAsia India.png";
import SpiceJet from "../assets/SpiceJet.png";

// Example grouped routes with multiple flights under each
// const routes = [
//     {
//         "route": "Chennai (MAA) → Pune (PNQ)",
//         "flights": [
//             {
//                 "price": "₹4,199.26",
//                 "id": 1,
//                 "airline": "GoAir",
//                 "segments": [
//                     {
//                         "duration": "1h 04m",
//                         "from": "Chennai (MAA)",
//                         "to": "Pune (PNQ)",
//                         "distance": "912.88 km"
//                     }
//                 ]
//             },
//             {
//                 "price": "₹4,016.69",
//                 "id": 2,
//                 "airline": "SpiceJet",
//                 "segments": [
//                     {
//                         "duration": "1h 04m",
//                         "from": "Chennai (MAA)",
//                         "to": "Pune (PNQ)",
//                         "distance": "912.88 km"
//                     }
//                 ]
//             },
//             {
//                 "price": "₹4,107.97",
//                 "id": 3,
//                 "airline": "AirAsia India",
//                 "segments": [
//                     {
//                         "duration": "1h 04m",
//                         "from": "Chennai (MAA)",
//                         "to": "Pune (PNQ)",
//                         "distance": "912.88 km"
//                     }
//                 ]
//             },
//             {
//                 "price": "₹4,381.84",
//                 "id": 4,
//                 "airline": "IndiGo",
//                 "segments": [
//                     {
//                         "duration": "1h 04m",
//                         "from": "Chennai (MAA)",
//                         "to": "Pune (PNQ)",
//                         "distance": "912.88 km"
//                     }
//                 ]
//             }
//         ]
//     },
//     {
//         "route": "Pune (PNQ) → Surat (STV)",
//         "flights": [
//             {
//                 "price": "₹1,352.18",
//                 "id": 1,
//                 "airline": "SpiceJet",
//                 "segments": [
//                     {
//                         "duration": "0h 22m",
//                         "from": "Pune (PNQ)",
//                         "to": "Surat (STV)",
//                         "distance": "307.31 km"
//                     }
//                 ]
//             }
//         ]
//     }
// ];

const airlineImages = {
    "Air India": AirIndia,
    "IndiGo": IndiGo,
    "GoAir": GoAir,
    "AirAsia India": AirAsiaIndia,
    "SpiceJet": SpiceJet
};

export default function FlightModal({ open, onClose,routes }) {
    if (!open) return null;

    return (
        <div id="flight-modal">
            <AnimatePresence>
                <motion.div
                    className="fixed inset-0 bg-black/40 flex items-center justify-center z-[99999]"
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    exit={{ opacity: 0 }}
                >
                    <motion.div
                        className="bg-white rounded-2xl shadow-xl max-w-5xl w-full mx-4 p-6 overflow-y-auto max-h-[90vh]"
                        initial={{ scale: 0.9, opacity: 0 }}
                        animate={{ scale: 1, opacity: 1 }}
                        exit={{ scale: 0.9, opacity: 0 }}
                        transition={{ duration: 0.3 }}
                    >
                        {/* Header */}
                        <div className="flex justify-between items-center border-b pb-3 mb-4">
                            <h2 className="text-xl font-semibold">✈ Available Flights</h2>
                            <button onClick={onClose}>
                                <X className="w-6 h-6 text-gray-600 hover:text-black" />
                            </button>
                        </div>

                        {/* Routes */}
                        <div className="space-y-8">
                            {routes.map((route, rIdx) => (
                                <div key={rIdx}>
                                    {/* Route Heading */}
                                    <h3 className="text-lg font-bold text-blue-700 mb-3">
                                        {route.route}
                                    </h3>

                                    {/* Flights under this route */}
                                    <div className="space-y-5">
                                        {route.flights.map((flight) => (
                                            <div
                                                key={flight.id}
                                                className="border rounded-xl p-5 hover:shadow-lg transition"
                                            >
                                                <div className="flex items-center justify-between mb-4">
                                                    <div className="flex items-center gap-3">
                                                        <img
                                                            src={airlineImages[flight.airline] || null}
                                                            alt={flight.airline}
                                                            className="w-20 h-20 object-contain"
                                                        />
                                                        <p className="font-medium text-lg">{flight.airline}</p>
                                                    </div>
                                                    <p className="text-xl font-bold text-green-600">
                                                        {flight.price} <span className="text-sm font-normal text-gray-500">(est.)</span>
                                                    </p>
                                                </div>


                                                <div className="space-y-4">
                                                    {flight.segments.map((seg, idx) => (
                                                        <div
                                                            key={idx}
                                                            className="flex flex-col md:flex-row md:items-center md:justify-between border rounded-lg p-3 bg-gray-50"
                                                        >
                                                            <div>
                                                                <p className="text-sm text-gray-500">
                                                                    {seg.from} → {seg.to}
                                                                </p>
                                                                <p className="text-sm">
                                                                    Duration:{" "}
                                                                    <span className="font-medium">{seg.duration}</span>
                                                                </p>
                                                            </div>
                                                            <div className="flex gap-6 mt-2 md:mt-0">
                                                                <div className="text-center">
                                                                    <p className="font-semibold">{seg.distance}</p>
                                                                    <p className="text-xs text-gray-500">Distance</p>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    ))}
                                                </div>

                                                {/* <div className="mt-4 flex justify-end">
                                                    <button className="px-5 py-2 bg-blue-600 text-white rounded-lg shadow hover:bg-blue-700 transition">
                                                        Book Now
                                                    </button>
                                                </div> */}
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            ))}
                        </div>
                    </motion.div>
                </motion.div>
            </AnimatePresence>
        </div>
    );
}

