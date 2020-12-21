import React from "react"
import classes from "./Menu.module.css"

export type MenuProps = {
    menuItems: JSX.Element[]
    isVisible: boolean
}
const Menu: React.FC<MenuProps> = (props) => {
    const output = (
        <div
            className={[
                classes.Menu,
                props.isVisible ? "" : classes.noDisplay
            ].join(" ")}
        >
            {props.menuItems}
        </div>
    )
    return output
}

export default Menu
