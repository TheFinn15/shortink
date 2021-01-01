import React from 'react';
import './App.css';
import {
  Avatar,
  Card,
  CardContent,
  CardHeader,
  Divider,
  Grid,
  IconButton,
  Menu, MenuItem,
  Typography
} from '@material-ui/core';
import {createStyles, makeStyles, Theme} from '@material-ui/core/styles';
import {MoreVertOutlined} from "@material-ui/icons";

// TODO: Сокращатель ссылок:
//  Полное управление ссылками и предпросмотр;
//  Мульти ссылки(скрещивание ссылок в одну, с возможностью перехода на каждую из них;
//  Лайф-лента ссылок;
//  Метод шифровки для ссылок и также мульти-ссылок.

const styles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      margin: theme.spacing(10, 15, 0)
    },
    title: {
      margin: '0 2% 0',
      justifyContent: 'center',
      display: 'flex'
    },
    liveCard: {
      margin: theme.spacing(5),
      backgroundColor: '#8e8e8e',
      color: '#ffffff'
    },
    liveCardHead: {
      textDecoration: 'none',
      color: '#ffffff'
    }
  })
);


export default function HomePage() {
  const [ancEl, setAncEl] = React.useState<null | HTMLElement>(null)
  const handleClick = (ev: React.MouseEvent<HTMLButtonElement>) => {
    setAncEl(ev.currentTarget)
  }
  const handleClose = () => {
    setAncEl(null)
  }

  const liveList = []
  for (let i = 0; i < 10; i++) {
    liveList.push(
      <Grid item sm={6} spacing={4} xs={12} xl={3}>
        <Menu anchorEl={ancEl} open={Boolean(ancEl)} onClose={handleClose}>
          <MenuItem>Keks</MenuItem>
          <MenuItem>Keks</MenuItem>
          <MenuItem>Keks</MenuItem>
        </Menu>
        <Card className={styles().liveCard} elevation={8}>
          <CardHeader className={styles().liveCardHead} title={'User_'+i} subheader={'test2@ma.com'} subheaderTypographyProps={{color: 'inherit'}} avatar={
            <Avatar/>
          } action={
            <IconButton onClick={handleClick}>
              <MoreVertOutlined/>
            </IconButton>
          }/>
          <Divider/>
          <CardContent>
            <Typography>
              someText_{i}
            </Typography>
          </CardContent>
        </Card>
      </Grid>
    )
  }
  return (
    <div>
      <Card className={styles().root}>
        <CardHeader title={'Лайф-лента ссылок'} style={{textAlign: 'center'}}/>
        <CardContent className={styles().title}>
          <Grid container>
            {liveList}
          </Grid>
        </CardContent>
      </Card>
    </div>
  );
};
