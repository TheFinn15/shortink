import {IconButton, List, Drawer, makeStyles, Divider} from '@material-ui/core';
import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import {ExitToApp, Menu, PersonAddRounded} from "@material-ui/icons";


export default function ToolbarDrawer() {
  const styles = makeStyles({
    list: {
      width: 250
    }
  })()

  const [state, setState] = React.useState(
    {
      left: false
    }
  )

  const toggleDrawer = (anchor: string, open: boolean) => (
    event: React.KeyboardEvent | React.MouseEvent
  ) => {
    if (
      event.type === 'keydown' &&
      ((event as React.KeyboardEvent).key === 'Tab' ||
        (event as React.KeyboardEvent).key === 'Shift')
    ) {
      return;
    }

    setState({ ...state, [anchor]: open });
  }

  const list = (
    <div
      className={styles.list}
      role="presentation"
      onClick={toggleDrawer("left", false)}>
      <List>
        <ListItem key="ИНФОРМАЦИЯ" style={{textAlign: 'center'}}>
          <ListItemText primary="ИНФОРМАЦИЯ"/>
        </ListItem>
        <Divider/>
        <ListItem button key="Авторизация">
          <ListItemIcon>
            <ExitToApp/>
          </ListItemIcon>
          <ListItemText primary="Авторизация"/>
        </ListItem>
        <ListItem button key="Регистрация">
          <ListItemIcon>
            <PersonAddRounded/>
          </ListItemIcon>
          <ListItemText primary="Регистрация"/>
        </ListItem>
      </List>
    </div>
  );

  return (
    <div>
      <React.Fragment key="left">
        <IconButton edge="start" color="inherit" onClick={toggleDrawer('left', true)}>
          <Menu/>
        </IconButton>
        <Drawer anchor="left" open={state['left']} onClose={toggleDrawer('left', false)}>
          {list}
        </Drawer>
      </React.Fragment>
    </div>
  )
}