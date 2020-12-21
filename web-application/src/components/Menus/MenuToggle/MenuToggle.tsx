import React from "react"
import classes from "./MenuToggle.module.css"

type MenuToggleProps = {
    clicked?: () => void
}
const MenuToggle: React.FC<MenuToggleProps> = (props) => {
    return (
        <div className={classes.MenuToggle} onClick={props.clicked}>
            <div></div>
            <div></div>
            <div></div>
        </div>
    )
}

export default MenuToggle
