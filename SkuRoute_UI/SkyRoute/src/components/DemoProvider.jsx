import React from "react";
import { TourProvider, useTour } from "@reactour/tour";
import GiggleButton from "./GiggleButton";
import SkyMap from "./SkyMap";

const steps = [
    {
        selector: ".source",
        content: (
            <span>
                Start here 👉 Pick your <strong>departure airport</strong> to begin planning your journey.
            </span>
        ),
        position: "bottom",
    },
    {
        selector: ".destination",
        content: (
            <span>
                Now choose your <strong>destination airport</strong> ✈️ to complete the route setup.
            </span>
        ),
        position: "bottom",
    },
    {
        selector: ".drawer",
        content: (
            <span>
                Great! 🎉 Here you’ll see the <strong>shortest routes</strong> and its details.
            </span>
        ),
        position: "left",
    },
    {
        selector: ".skymaphome",
        content: (
            <span>
                Click here anytime to return <strong>back to Home</strong> 🏠
            </span>
        ),
        position: "right",
    },
    {
        selector: "body",
        content: (
            <span>
                You're all set! 🚀 Go ahead and explore <strong>SkyRoute</strong>. You can always reopen this demo with the help button below.
            </span>
        ),
        position: "center",
    },
];


// 🔹 A child component where we can safely use `useTour`
function DemoControls() {
    const { setIsOpen , setCurrentStep} = useTour();

    const openDemo = () => {
        console.log("In the demo..");
        setCurrentStep(0);
        setIsOpen(true);
    };

    return <GiggleButton openDemo={openDemo} />;
}

export default function DemoProvider() {
    return (
        <TourProvider
            steps={steps}
            styles={{
                popover: (base) => ({
                    ...base,
                    borderRadius: 12,
                    padding: "20px 40px",
                    backgroundColor: "#fff",
                    boxShadow: "0 6px 20px rgba(0,0,0,0.15)",
                    maxWidth: 260, // prevents too-wide boxes
                }),
                maskArea: (base) => ({
                    ...base,
                    rx: 8, // smooth rounded corners
                }),
                maskWrapper: (base) => ({
                    ...base,
                    color: "rgba(0,0,0,0.65)", // slightly darker overlay
                }),
                badge: (base) => ({
                    ...base,
                    backgroundColor: "#2563eb", // Tailwind blue-600
                    fontSize: "13px",
                    padding: "2px 8px",
                    borderRadius: 6,
                }),
                close: (base) => ({
                    ...base,
                    color: "#374151", // gray-700
                    fontSize: "14px",
                }),
            }}
        >
            <SkyMap />
            <DemoControls />
        </TourProvider>

    );
}

