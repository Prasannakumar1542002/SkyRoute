import React, { useState } from "react";

const AboutUsModal = ({ isOpen, setIsOpen }) => {

    return (
        <>
            {/* Button to open modal */}
            <button
                onClick={() => setIsOpen(true)}
                className="mt-6 px-8 py-4 bg-yellow-400 text-gray-900 font-semibold rounded-lg shadow-lg hover:bg-yellow-500 transition"
            >
                About Us
            </button>

            {/* Modal overlay */}
            {/* Modal */}
            {isOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-[999999]">
                    <div className="relative bg-white rounded-2xl shadow-2xl max-w-2xl w-full p-8 z-[1000000]">
                        {/* Close button */}
                        <button
                            onClick={() => setIsOpen(false)}
                            className="absolute top-4 right-4 text-gray-500 hover:text-gray-800"
                        >
                            ✕
                        </button>

                        {/* Title */}
                        <h2 className="text-2xl font-extrabold text-gray-900 mb-4 text-center">
                            About Us
                        </h2>

                        {/* Content */}
                        <p className="text-gray-700 leading-relaxed text-center">
                            We are passionate about making air travel smarter and simpler.
                            Our system helps travelers and aviation enthusiasts discover the
                            most efficient flight routes from any source to destination,
                            backed by clear mapping and seamless visualization.
                        </p>
                        <p className="mt-4 text-gray-700 leading-relaxed text-center">
                            This application leverages <span className="font-semibold">Dijkstra's Algorithm </span>
                            to determine the shortest path by providing routes with the fewest connection stops
                            to the destination, showcasing the application of graph theory in real-world
                            travel network optimization.
                        </p><br />
                        <p className="text-sm text-gray-500 italic text-center mb-2">
                            *The airline details displayed are based on the 2024 aviation reports and flight data.*
                        </p>
                    </div>
                </div>
            )}

        </>
    );
};

export default AboutUsModal;
