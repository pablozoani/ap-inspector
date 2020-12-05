import classes from "./MenuItem.module.css"
import React from "react"

export type MenuItemProps = {
    style?: React.CSSProperties | undefined
    display?: boolean
    classes?: string | null
    clicked?: () => void
}

const MenuItem: React.FC<MenuItemProps> = (props) => {
    return (
        <div
            onClick={props.clicked}
            className={[
                classes.MenuItem,
                props.classes,
                props.display === false ? null : classes.noDisplay
            ].join(" ")}
            style={props.style}
        >
            {props.children}
        </div>
    )
}

export default MenuItem
