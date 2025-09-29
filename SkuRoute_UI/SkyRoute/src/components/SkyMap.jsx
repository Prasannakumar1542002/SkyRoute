import React, { useEffect, useMemo, useRef, useState } from "react";
import { MapContainer, TileLayer, Marker, Popup, useMap, ZoomControl } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import logo from "../assets/logo2.png";
import antenna from "../assets/broadcast_rm.gif";
import source from "../assets/source2.png";
import destination from "../assets/destinationMarker.png";
import airplane from "../assets/flight.png";
import L from "leaflet";
import { Polyline } from "react-leaflet";
import "leaflet-ant-path";
import { antPath } from "leaflet-ant-path";
import "leaflet-rotatedmarker";
import AirportSelect from "./AirportSelect";
import "animate.css";
import Swal from "sweetalert2";


const customIcon = L.icon({
  iconUrl: logo,
  iconSize: [32, 32],
  iconAnchor: [16, 32],
  popupAnchor: [0, -32]
});

const sourceIcon = L.icon({
  iconUrl: source,
  iconSize: [32, 32],
  iconAnchor: [16, 32],
  popupAnchor: [0, -32]
});

const destinationIcon = L.icon({
  iconUrl: destination,
  iconSize: [32, 32],
  iconAnchor: [16, 32],
  popupAnchor: [0, -32]
});

// Small helper to animate to a new position
const FlyTo = ({ center, zoom }) => {
  const map = useMap();
  useEffect(() => {
    if (center) map.flyTo(center, zoom ?? 14, { duration: 0.8 });
  }, [center, zoom, map]);
  return null;
};

const SkyMap = () => {
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [query, setQuery] = useState("");
  const [search, setSearch] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const [selectedPos, setSelectedPos] = useState({ lat: 28.6139, lng: 77.209 }); // New Delhi
  const [flyTarget, setFlyTarget] = useState(null);
  const [base, setBase] = useState("osm"); // "osm" | "hot"
  const [routes, setRoutes] = useState([]);
  const [airports, setAirports] = useState([]);


  const [selectedSource, setSelectedSource] = useState("");
  const [selectedDestination, setSelectedDestination] = useState("");

  // Fetch airports from API
  useEffect(() => {
    fetch("/skyroute/get-airports")   // 🔹 replace with your endpoint
      .then(res => res.json())
      .then(data => {
        setAirports(data);  // expect API to return array of { code, name, city }
      })
      .catch(err => console.error("Error fetching airports:", err));
  }, []);

  // fetch the JSON from public folder
  useEffect(() => {
    console.log("selectedSource : ", selectedSource);
    console.log("selectedDestination : ", selectedDestination);
    if (selectedDestination == null || selectedDestination == "" || selectedDestination == undefined) {
      setRoutes([]);
    }
    if ((selectedSource == null || selectedSource == "" || selectedSource == undefined) && (selectedDestination != null && selectedDestination != "" && selectedDestination != undefined)) {
      // alert("Please Choose the Source");
      Swal.fire({
        icon: "error",
        title: "<span style='color:#e74c3c; font-size:24px; font-weight:700;'>Error</span>",
        html: "<p style='font-size:16px; color:#444;'>Please choose the source first.</p>",
        background: "#fff",
        showClass: {
          popup: "animate__animated animate__zoomIn faster"
        },
        hideClass: {
          popup: "animate__animated animate__fadeOutUp faster"
        },
        iconColor: "#e74c3c",
        confirmButtonText: "Got it",
        buttonsStyling: false,
        customClass: {
          popup: "rounded-2xl shadow-2xl p-6",
          confirmButton:
            "bg-red-600 hover:bg-red-700 text-white px-6 py-2 rounded-lg font-semibold transition-all",
          icon: "animate__animated animate__shakeX animate__faster", // 🔥 animate the icon
        },
      });


      setSelectedDestination("");
      setRoutes([]);
    } else {
      fetch("/skyroute/get-shortest-path", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          "origin": selectedSource,
          "destination": selectedDestination
        })
      })
        .then((res) => res.json())
        .then((data) => {
          if (data?.routes) {
            setRoutes(data.routes);
            data.routes && data.routes.length == 0 ? Swal.fire({
              icon: "error",
              title: "<span style='color:#e74c3c; font-size:24px; font-weight:700;'>Error</span>",
              html: "<p style='font-size:16px; color:#444;'>No flights available from " + getCityNameFromCode(selectedSource) + " to " + getCityNameFromCode(selectedDestination) + "</p>",
              background: "#fff",
              showClass: {
                popup: "animate__animated animate__zoomIn faster"
              },
              hideClass: {
                popup: "animate__animated animate__fadeOutUp faster"
              },
              iconColor: "#e74c3c",
              confirmButtonText: "Got it",
              buttonsStyling: false,
              customClass: {
                popup: "rounded-2xl shadow-2xl p-6",
                confirmButton:
                  "bg-red-600 hover:bg-red-700 text-white px-6 py-2 rounded-lg font-semibold transition-all",
                icon: "animate__animated animate__shakeX animate__faster", // 🔥 animate the icon
              },
            }) : ""
            data.routes && data.routes.length == 0 ? setSelectedDestination("") : ""
            data.routes && data.routes.length == 0 ? setSelectedSource("") : ""
          }
          console.log(selectedSource + " : " + selectedDestination);

          if (selectedSource == selectedDestination && selectedSource != null && selectedDestination != null && selectedSource != undefined && selectedDestination != undefined && selectedSource != "" && selectedDestination != "") {
            Swal.fire({
              icon: "error",
              title: "<span style='color:#e74c3c; font-size:24px; font-weight:700;'>Error</span>",
              html: "<p style='font-size:16px; color:#444;'>Source and Destination cannot be same</p>",
              background: "#fff",
              showClass: {
                popup: "animate__animated animate__zoomIn faster"
              },
              hideClass: {
                popup: "animate__animated animate__fadeOutUp faster"
              },
              iconColor: "#e74c3c",
              confirmButtonText: "Got it",
              buttonsStyling: false,
              customClass: {
                popup: "rounded-2xl shadow-2xl p-6",
                confirmButton:
                  "bg-red-600 hover:bg-red-700 text-white px-6 py-2 rounded-lg font-semibold transition-all",
                icon: "animate__animated animate__shakeX animate__faster", // 🔥 animate the icon
              },
            });
            setSelectedDestination("");
            setSelectedSource("");
          }


        })
        .catch((err) => console.error("Error loading routes:", err));
    }
  }, [selectedDestination]);

  useEffect(() => {
    console.log("selectedSource : ", selectedSource);
    console.log("selectedDestination : ", selectedDestination);
    if (selectedSource == null || selectedSource == "" || selectedSource == undefined) {
      setRoutes([]);
    }
    if (selectedDestination != null && selectedDestination != "" && selectedDestination != undefined) {
      fetch("/skyroute/get-shortest-path", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          "origin": selectedSource,
          "destination": selectedDestination
        })
      })
        .then((res) => res.json())
        .then((data) => {
          if (data?.routes) {
            setRoutes(data.routes);
            data.routes && data.routes.length == 0 ? Swal.fire({
              icon: "error",
              title: "<span style='color:#e74c3c; font-size:24px; font-weight:700;'>Error</span>",
              html: "<p style='font-size:16px; color:#444;'>No flights available from " + getCityNameFromCode(selectedSource) + " to " + getCityNameFromCode(selectedDestination) + "</p>",
              background: "#fff",
              showClass: {
                popup: "animate__animated animate__zoomIn faster"
              },
              hideClass: {
                popup: "animate__animated animate__fadeOutUp faster"
              },
              iconColor: "#e74c3c",
              confirmButtonText: "Got it",
              buttonsStyling: false,
              customClass: {
                popup: "rounded-2xl shadow-2xl p-6",
                confirmButton:
                  "bg-red-600 hover:bg-red-700 text-white px-6 py-2 rounded-lg font-semibold transition-all",
                icon: "animate__animated animate__shakeX animate__faster", // 🔥 animate the icon
              },
            }) : ""
          }
          if (selectedSource == selectedDestination && selectedSource != null && selectedDestination != null && selectedSource != undefined && selectedDestination != undefined && selectedSource != "" && selectedDestination != "") {
            Swal.fire({
              icon: "error",
              title: "<span style='color:#e74c3c; font-size:24px; font-weight:700;'>Error</span>",
              html: "<p style='font-size:16px; color:#444;'>Source and Destination cannot be same</p>",
              background: "#fff",
              showClass: {
                popup: "animate__animated animate__zoomIn faster"
              },
              hideClass: {
                popup: "animate__animated animate__fadeOutUp faster"
              },
              iconColor: "#e74c3c",
              confirmButtonText: "Got it",
              buttonsStyling: false,
              customClass: {
                popup: "rounded-2xl shadow-2xl p-6",
                confirmButton:
                  "bg-red-600 hover:bg-red-700 text-white px-6 py-2 rounded-lg font-semibold transition-all",
                icon: "animate__animated animate__shakeX animate__faster", // 🔥 animate the icon
              },
            });
            setSelectedDestination("");
            setSelectedSource("");
          }

        })
        .catch((err) => console.error("Error loading routes:", err));
    }
  }, [selectedSource]);

  const inputRef = useRef(null);
  const debounceRef = useRef(null);

  useEffect(() => {
    if (selectedPos?.lat && selectedPos?.lng) {
      fetch(
        `https://nominatim.openstreetmap.org/reverse?lat=${selectedPos.lat}&lon=${selectedPos.lng}&format=json`,
        {
          headers: {
            "User-Agent": "SkyMapApp/1.0",
          },
        }
      )
        .then((res) => res.json())
        .then((data) => {
          console.log("data : ", data);
          console.log("state : ", data?.address?.state);
          console.log("country : ", data?.address?.country);

          if (data?.address?.state && data?.address?.country) {
            setQuery(data.address.state, data.address.country);
          } else {
            setQuery(data?.address?.country || "Unknown location");
          }
        })
        .catch(() => setQuery("Error fetching location"));
    }
  }, [selectedPos]);


  // Debounced fetch to Nominatim (OpenStreetMap) for suggestions
  useEffect(() => {
    if (debounceRef.current) clearTimeout(debounceRef.current);
    if (!search?.trim()) {
      setSuggestions([]);
      return;
    }
    debounceRef.current = setTimeout(async () => {
      try {
        const url = `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(
          search.trim()
        )}&format=json&addressdetails=1&limit=6`;
        const res = await fetch(url, {
          headers: {
            // polite headers; don't set User-Agent from browser
            "Accept-Language": "en",
          },
        });
        const data = await res.json();
        const items = (data || []).map((d) => ({
          id: d.place_id,
          name: d.display_name,
          lat: parseFloat(d.lat),
          lon: parseFloat(d.lon),
        }));
        setSuggestions(items);
      } catch {
        setSuggestions([]);
      }
    }, 300); // debounce 300ms
    return () => clearTimeout(debounceRef.current);
  }, [search]);

  const onPickSuggestion = (s) => {
    const center = { lat: s.lat, lng: s.lon };
    setSelectedPos(center);
    setFlyTarget(center);
    setSuggestions([]);
    setSearch(s.name);
  };

  const getCityNameFromCode = (code) => {
    return Object.keys(airports).find(city => airports[city] === code);
  };

  const tiles = useMemo(
    () => ({
      osm: {
        name: "OSM Standard",
        url: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
        attribution:
          '&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
      },
      hot: {
        name: "OSM Humanitarian",
        url: "https://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png",
        attribution:
          '&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, Tiles style by Humanitarian OpenStreetMap Team',
      },
    }),
    []
  );

  function calculateDistance(lat1, lon1, lat2, lon2) {
    const R = 6371; // 🌍 Earth radius in kilometers

    const toRad = (value) => (value * Math.PI) / 180;

    const dLat = toRad(lat2 - lat1);
    const dLon = toRad(lon2 - lon1);

    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(toRad(lat1)) *
      Math.cos(toRad(lat2)) *
      Math.sin(dLon / 2) *
      Math.sin(dLon / 2);

    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c; // 📏 distance in kilometers
  }


  return (
    <div style={styles.page}>
      {/* Top Bar with Search */}
      <div style={styles.topbar}>
        <div className="skymaphome" style={styles.brand}>
          <span>
            <img style={{ paddingBottom: "3px" }} src={antenna} alt="antenna" className="w-6 h-6" />
          </span>
          <span style={styles.brandText}><a href="/">SkyMap</a></span>
        </div>
        <div style={styles.searchWrap}>
          <AirportSelect
            selectedSource={selectedSource}
            setSelectedSource={setSelectedSource}
            selectedDestination={selectedDestination}
            setSelectedDestination={setSelectedDestination}
            airports={airports}   // coming from your useAirports hook
          />
        </div>
        {/* Right-side actions */}
        <div style={styles.actions}>
          <button
            className="drawer"
            style={styles.roundBtn}
            title="Layers"
            onClick={() => setDrawerOpen((v) => !v)}
          >
            🗺
          </button>
        </div>
      </div>

      {/* Drawer (Right) */}
      <div style={{ ...styles.drawer, ...(drawerOpen ? styles.drawerOpen : {}) }}>
        <div style={styles.drawerHeader}>
          <h3 style={styles.drawerTitle}>Route Preview</h3>
        </div>

        <div style={styles.routesWrap}>
          {routes.length > 0 ? (
            routes.map((route, idx) => (
              <div key={idx} style={styles.routeCard}>
                <div style={styles.routeHeader}>Route {idx + 1}</div>
                {route.map((airport, i) => (
                  <div key={airport.IATA_code} style={styles.routeStep}>
                    {/* Circle */}
                    <div style={styles.stepWrapper}>
                      <div style={styles.stepCircle}>
                        {i === 0 ? "S" : i === route.length - 1 ? "D" : i}
                      </div>
                      {i < route.length - 1 && <div style={styles.stepLine}></div>}
                    </div>

                    {/* Airport info */}
                    <div style={styles.stepContent}>
                      <div style={styles.stepTitle}>{airport.airport_name}</div>
                      <div style={styles.stepSub}>
                        {airport.city_name} ({airport.IATA_code})
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            ))
          ) : (
            <div style={{ fontSize: 14, color: "#6b7280", padding: "12px" }}>
              Select a source and destination to see the route here.
            </div>
          )}
        </div>

        <div style={styles.drawerFooter}>
          SkyRoute – Navigate Smarter, Travel Further
        </div>

      </div>



      {/* Map */}
      <div style={styles.mapWrap}>
        <MapContainer
          center={[13, 80]} // center near Chennai initially
          zoom={5}
          style={{ height: "100%", width: "100%" }}
          zoomControl={false}
          attributionControl={false}
        >
          <ZoomControl position="topright" />
          <TileLayer url={tiles[base].url} attribution={tiles[base].attribution} />

          {/* Loop through routes */}
          {routes.map((route, idx) => (
            <React.Fragment key={idx}>
              {/* Airport markers */}
              {route.map((airport, i) => (
                <Marker
                  key={airport.IATA_code + i}
                  position={[parseFloat(airport.latitude), parseFloat(airport.longitude)]}
                  icon={i == 0 ? sourceIcon : i == route.length - 1 ? destinationIcon : customIcon}
                >
                  <Popup>
                    <strong>{airport.airport_name}</strong>
                    <div>{airport.city_name}</div>
                    <div>IATA: {airport.IATA_code}</div>
                  </Popup>
                </Marker>
              ))}

              {/* Flight legs */}
              {route.map((airport, i) => {
                if (i === route.length - 1) return null;
                const source = {
                  lat: parseFloat(route[i].latitude),
                  lng: parseFloat(route[i].longitude),
                };
                const dest = {
                  lat: parseFloat(route[i + 1].latitude),
                  lng: parseFloat(route[i + 1].longitude),
                };
                return (
                  <React.Fragment key={i}>
                    <AntPathLine source={source} destination={dest} />
                    <FlightMarker route={route} />
                  </React.Fragment>
                );
              })}
            </React.Fragment>
          ))}

          {/* Fit map bounds dynamically to all airports */}
          {routes.length > 0 && (
            <FitBounds
              source={{
                lat: Math.min(...routes.flat().map((a) => parseFloat(a.latitude))),
                lng: Math.min(...routes.flat().map((a) => parseFloat(a.longitude))),
              }}
              destination={{
                lat: Math.max(...routes.flat().map((a) => parseFloat(a.latitude))),
                lng: Math.max(...routes.flat().map((a) => parseFloat(a.longitude))),
              }}
            />
          )}
        </MapContainer>
      </div>
    </div>
  );
};

/* Inline, modern styles */
const styles = {
  routesWrap: {
    flex: 1,
    overflowY: "auto",
    padding: "12px 16px",
    display: "flex",
    flexDirection: "column",
    gap: 16,
  },

  routeCard: {
    background: "#fff",
    borderRadius: 14,
    padding: "14px 16px",
    boxShadow: "0 4px 12px rgba(0,0,0,0.08)",
    display: "flex",
    flexDirection: "column",
    gap: 12,
  },

  routeHeader: {
    fontSize: 13,
    fontWeight: 700,
    color: "#374151",
    marginBottom: 6,
    borderBottom: "1px solid #e5e7eb",
    paddingBottom: 4,
  },

  routeStep: {
    display: "flex",
    alignItems: "flex-start",
    gap: 12,
    position: "relative",
  },

  stepWrapper: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },

  stepCircle: {
    width: 28,
    height: 28,
    borderRadius: "50%",
    background: "linear-gradient(135deg, #3b82f6, #6366f1)",
    color: "#fff",
    fontWeight: "bold",
    fontSize: 13,
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    flexShrink: 0,
    zIndex: 1,
  },

  stepLine: {
    width: 2,
    flex: 1,
    background: "#d1d5db",
    marginTop: 2,
  },

  stepContent: {
    display: "flex",
    flexDirection: "column",
    marginTop: 2,
  },

  stepTitle: {
    fontSize: 14,
    fontWeight: 600,
    color: "#111827",
  },

  stepSub: {
    fontSize: 12,
    color: "#6b7280",
  },

  page: {
    position: "relative",
    height: "100vh",
    width: "100%",
    overflow: "hidden",
    fontFamily: "Inter, system-ui, -apple-system, Segoe UI, Roboto, Arial, sans-serif",
    background: "#eef2f7",
  },

  /* Top bar */
  topbar: {
    position: "absolute",
    top: 12,
    left: 12,
    right: 12,
    display: "flex",
    alignItems: "center",
    gap: 12,
    zIndex: 1200,
    width: "96%"
  },
  brand: {
    display: "flex",
    alignItems: "center",
    gap: 2,
    padding: "8px 12px",
    borderRadius: 12,
    background: "rgba(255,255,255,0.9)",
    boxShadow: "0 4px 16px rgba(0,0,0,0.08)",
    backdropFilter: "blur(8px)",
  },
  brandDot: {
    width: 10,
    height: 10,
    borderRadius: "50%",
    background:
      "linear-gradient(135deg, rgba(59,130,246,1) 0%, rgba(99,102,241,1) 100%)",
  },
  brandText: { fontWeight: 700, letterSpacing: 0.2 },

  searchWrap: {
    position: "relative",
    flex: 1,
    display: "flex",
    alignItems: "center",
  },
  searchInput: {
    width: "100%",
    padding: "12px 14px",
    borderRadius: 12,
    border: "1px solid rgba(0,0,0,0.08)",
    outline: "none",
    background: "rgba(255,255,255,0.95)",
    boxShadow: "0 8px 24px rgba(0,0,0,0.08)",
    fontSize: 14,
  },
  suggestBox: {
    position: "absolute",
    top: "100%",
    left: 0,
    right: 0,
    marginTop: 6,
    background: "rgba(255,255,255,0.98)",
    borderRadius: 12,
    boxShadow: "0 12px 30px rgba(0,0,0,0.12)",
    overflow: "hidden",
    zIndex: 1300,
  },
  suggestItem: {
    display: "block",
    width: "100%",
    textAlign: "left",
    padding: "10px 12px",
    border: "none",
    background: "transparent",
    cursor: "pointer",
    fontSize: 14,
  },

  actions: { display: "flex", alignItems: "center", marginLeft: 6 },
  roundBtn: {
    height: 40,
    minWidth: 40,
    padding: "0 12px",
    borderRadius: 12,
    border: "1px solid rgba(0,0,0,0.06)",
    background: "rgba(255,255,255,0.9)",
    boxShadow: "0 8px 24px rgba(0,0,0,0.08)",
    cursor: "pointer",
    fontSize: 18
  },

  /* Drawer (right, glassmorphism) */
  drawer: {
    position: "absolute",
    top: "83px",
    right: -340,
    width: 320,
    height: "83%",
    display: "flex",
    flexDirection: "column",
    background: "rgba(255,255,255,0.65)",
    backdropFilter: "blur(14px)",
    borderLeft: "1px solid rgba(255,255,255,0.6)",
    boxShadow: "-12px 0 30px rgba(0,0,0,0.12)",
    transition: "right 0.35s cubic-bezier(.2,.8,.2,1)",
    zIndex: 1100,
    borderTopLeftRadius: 16,
    borderBottomLeftRadius: 16,
    paddingBottom: 12,
  },
  drawerOpen: { right: 12 }, // slight gap from edge = premium feel
  drawerHeader: {
    padding: "16px 18px",
    borderBottom: "1px solid rgba(0,0,0,0.06)",
  },
  drawerTitle: {
    margin: 0,
    fontSize: 16,
    fontWeight: 700,
    color: "#111827",
    letterSpacing: 0.2,
  },
  section: { padding: "16px 18px" },
  sectionLabel: {
    fontSize: 12,
    fontWeight: 700,
    textTransform: "uppercase",
    letterSpacing: 0.8,
    color: "#6b7280",
    marginBottom: 10,
  },
  segment: {
    display: "inline-flex",
    borderRadius: 12,
    border: "1px solid rgba(0,0,0,0.08)",
    overflow: "hidden",
    boxShadow: "0 4px 12px rgba(0,0,0,0.06)",
  },
  segmentBtn: {
    padding: "10px 14px",
    background: "rgba(255,255,255,0.85)",
    border: "none",
    cursor: "pointer",
    fontSize: 13,
  },
  segmentActive: {
    background:
      "linear-gradient(135deg, rgba(59,130,246,0.25) 0%, rgba(99,102,241,0.25) 100%)",
    borderBottom: "2px solid rgba(99,102,241,0.8)",
  },
  pillRow: { display: "flex", gap: 8, flexWrap: "wrap" },
  pill: {
    padding: "8px 12px",
    borderRadius: 999,
    border: "1px solid rgba(0,0,0,0.08)",
    background: "rgba(255,255,255,0.9)",
    cursor: "pointer",
    fontSize: 13,
    boxShadow: "0 6px 16px rgba(0,0,0,0.06)",
  },
  drawerFooter: {
    padding: "12px 18px",
    fontSize: 12,
    color: "#6b7280",
    borderTop: "1px solid rgba(0,0,0,0.06)",
    textAlign: "center"
  },

  /* Map container area */
  mapWrap: {
    position: "absolute",
    inset: 0,
  },
};
//[19.076, 72.8777]
const AntPathLine = ({ source, destination }) => {
  const map = useMap();
  const pathRef = useRef(null);

  useEffect(() => {
    if (!source || !destination) return;

    // remove old path if exists
    if (pathRef.current) {
      map.removeLayer(pathRef.current);
    }

    const path = antPath(
      [
        [source.lat, source.lng],
        [destination.lat, destination.lng],
      ],
      {
        delay: 400,
        dashArray: [15, 20],
        weight: 5,
        color: "#2563eb",
        pulseColor: "#60a5fa",
        hardwareAccelerated: true,
      }
    );

    path.addTo(map);
    pathRef.current = path;

    return () => {
      if (pathRef.current) {
        map.removeLayer(pathRef.current);
      }
    };
  }, [source, destination, map]);

  return null;
};


const FlightMarker = ({ route }) => {
  const markerRef = useRef(null);

  useEffect(() => {
    if (!route || route.length < 2) return;

    const totalSteps = 1000; // total animation frames for the full path
    let step = 0;

    const interval = setInterval(() => {
      // Reset to loop again
      if (step >= totalSteps) {
        step = 0;
      }

      // Find which segment of the route we’re in
      const segmentIndex = Math.floor((step / totalSteps) * (route.length - 1));
      const segProgress =
        ((step / totalSteps) * (route.length - 1)) - segmentIndex;

      const source = {
        lat: parseFloat(route[segmentIndex].latitude),
        lng: parseFloat(route[segmentIndex].longitude),
      };
      const destination = {
        lat: parseFloat(route[segmentIndex + 1].latitude),
        lng: parseFloat(route[segmentIndex + 1].longitude),
      };

      // Linear interpolation
      const lat = source.lat + (destination.lat - source.lat) * segProgress;
      const lng = source.lng + (destination.lng - source.lng) * segProgress;

      const bearing = getBearing(
        source.lat,
        source.lng,
        destination.lat,
        destination.lng
      );

      if (markerRef.current) {
        markerRef.current.setLatLng([lat, lng]);
        markerRef.current.setRotationAngle(bearing);
        markerRef.current.setRotationOrigin("center center");
      }

      step++;
    }, 50);

    return () => clearInterval(interval);
  }, [route]);

  const planeIcon = L.icon({
    iconUrl: airplane,
    iconSize: [40, 40],
    iconAnchor: [20, 20],
  });

  return (
    <Marker
      ref={markerRef}
      position={[
        parseFloat(route[0].latitude),
        parseFloat(route[0].longitude),
      ]}
      icon={planeIcon}
    />
  );
};






function getBearing(lat1, lon1, lat2, lon2) {
  const toRad = (deg) => (deg * Math.PI) / 180;
  const toDeg = (rad) => (rad * 180) / Math.PI;

  const dLon = toRad(lon2 - lon1);
  lat1 = toRad(lat1);
  lat2 = toRad(lat2);

  const y = Math.sin(dLon) * Math.cos(lat2);
  const x =
    Math.cos(lat1) * Math.sin(lat2) -
    Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);

  return (toDeg(Math.atan2(y, x)) + 360) % 360;
}

function FitBounds({ source, destination }) {
  const map = useMap();

  useEffect(() => {
    if (source && destination) {
      const bounds = L.latLngBounds(
        [source.lat, source.lng],
        [destination.lat, destination.lng]
      );
      map.fitBounds(bounds, { padding: [50, 50] }); // padding for better view
    }
  }, [map, source, destination]);

  return null;
}



export default SkyMap;