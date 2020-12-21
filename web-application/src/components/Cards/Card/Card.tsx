import React from "react"
import classes from "./Card.module.css"

type CardProps = { title?: string }
const Card: React.FC<CardProps> = (props) => {
    return (
        <div className={classes.Card}>
            {props.title ? <div className={classes.CardTitle}>{props.title}</div> : null}
            <div className={classes.CardBody}>{props.children}</div>
        </div>
    )
}

export default Card
