const styles = {
    dropdown: {
        padding: "10px 14px",
        borderRadius: "8px",
        border: "1px solid #ccc",
        fontSize: "14px",
        minWidth: "220px",
        backgroundColor: "#fff",
        cursor: "pointer",
        appearance: "none",
        WebkitAppearance: "none",
        MozAppearance: "none",
        boxShadow: "0 3px 6px rgba(0,0,0,0.1)",
        transition: "all 0.3s ease",
        backgroundImage:
            "url('data:image/svg+xml;utf8,<svg fill=\"%23333\" height=\"24\" viewBox=\"0 0 24 24\" width=\"24\" xmlns=\"http://www.w3.org/2000/svg\"><path d=\"M7 10l5 5 5-5z\"/></svg>')",
        backgroundRepeat: "no-repeat",
        backgroundPosition: "right 10px center",
        backgroundSize: "16px",
        outline: "none",
        width: "100%",        // allow dropdown to scale
        maxWidth: "300px",    // optional max width
        boxSizing: "border-box",
    },
    dropdownHover: {
        borderColor: "#007BFF",
        boxShadow: "0 4px 8px rgba(0,123,255,0.3)",
    },
    wrapper: {
        display: "flex",
        gap: "14px",
        margin: "10px 0",
        flexWrap: "wrap",      // allow wrapping on very small screens
    },
    dropdownContainer: {
        flex: "1 1 220px",     // maintain min width but allow shrinking
    },
};

import { useState } from "react";

const AirportSelect = ({
    selectedSource,
    setSelectedSource,
    selectedDestination,
    setSelectedDestination,
    airports,
}) => {
    const [hovered, setHovered] = useState(null);

    const getStyle = (field) =>
        hovered === field
            ? { ...styles.dropdown, ...styles.dropdownHover }
            : styles.dropdown;

    return (
        <div style={styles.wrapper}>
            {/* Source */}
            <div style={styles.dropdownContainer}>
                <select
                    className="source"
                    value={selectedSource}
                    onChange={(e) => setSelectedSource(e.target.value)}
                    style={getStyle("source")}
                    onMouseEnter={() => setHovered("source")}
                    onMouseLeave={() => setHovered(null)}
                >
                    <option value="">Select Source</option>
                    {Object.entries(airports).map(([city, code]) => (
                        <option key={code} value={code}>
                            ✈ {city} ({code})
                        </option>
                    ))}
                </select>
            </div>

            {/* Destination */}
            <div style={styles.dropdownContainer}>
                <select
                    className="destination"
                    value={selectedDestination}
                    onChange={(e) => setSelectedDestination(e.target.value)}
                    style={getStyle("destination")}
                    onMouseEnter={() => setHovered("destination")}
                    onMouseLeave={() => setHovered(null)}
                >
                    <option value="">Select Destination</option>
                    {Object.entries(airports).map(([city, code]) => (
                        <option key={code} value={code}>
                            ✈ {city} ({code})
                        </option>
                    ))}
                </select>
            </div>
        </div>
    );
};

export default AirportSelect;
