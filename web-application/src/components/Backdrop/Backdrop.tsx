import React from "react"
import classes from "./Backdrop.module.css"

export type BackdropProps = {
    clicked: () => void
}
const Backdrop: React.FC<BackdropProps> = (props) => {
    return (
        <div className={classes.Backdrop} onClick={props.clicked}>
            {props.children}
        </div>
    )
}

export default Backdrop
