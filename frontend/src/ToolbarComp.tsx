import {AppBar, IconButton, Typography, Toolbar} from "@material-ui/core";
import {Menu} from "@material-ui/icons";
import React from "react";
import HomePage from "./HomePage";
import {Link} from "react-router-dom";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import ToolbarDrawer from "./ToolbarDrawer";


const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      backgroundColor: '#373737'
    },
    titleLogo: {
      color: 'white',
      textDecoration: 'none'
    }
  })
)

function ToolbarComp() {
    const styles = useStyles()
    return (
        <AppBar className={styles.root}>
          <Toolbar>
            <ToolbarDrawer/>
            <Link to="/" className={styles.titleLogo}>
              <Typography variant="h6">
                  Shortink
              </Typography>
            </Link>
          </Toolbar>
        </AppBar>
    );
}

export default ToolbarComp;