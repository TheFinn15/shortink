import React from 'react';
import './App.css';
import {Avatar, Card, CardContent, CardHeader, Grid, IconButton, Typography} from '@material-ui/core';
import {createStyles, makeStyles, Theme} from '@material-ui/core/styles';
import {Person} from "@material-ui/icons";
import {Link} from "react-router-dom";

// TODO: Сокращатель ссылок:
//  Полное управление ссылками и предпросмотр;
//  Мульти ссылки(скрещивание ссылок в одну, с возможностью перехода на каждую из них;
//  Лайф-лента ссылок;
//  Метод шифровки для ссылок и также мульти-ссылок.

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      margin: theme.spacing(10, 15, 0)
    },
    title: {
      margin: '0 2% 0',
      justifyContent: 'center',
      display: 'flex'
    }
  })
);

function getT(): any {
  let arr = []
  for (let i = 0; i < 10; i++) {
    arr.push(
      <Grid item sm={6} spacing={4}>
        <Card style={{margin: 5+'%', maxWidth: 100+'%'}}>
          <Link to={'/profile/user_'+i} style={{textDecoration: 'none', color: 'purple'}}>
            <CardHeader title={'User_'+i} subheader={'test2@ma.com'} avatar={
              <Avatar/>
            }/>
          </Link>
          <CardContent>
            <Typography>
              {i}
            </Typography>
          </CardContent>
        </Card>
      </Grid>
    )
  }
  return arr
}

function HomePage() {
  const styles = useStyles()
  return (
    <div>
      <Card className={styles.root}>
        <CardContent className={styles.title}>
          <Grid container>
            <Grid item>
              <CardHeader title={'Лайф-лента ссылок'} style={{textAlign: 'center'}}/>
            </Grid>
            <Grid>
              {getT()}
            </Grid>
          </Grid>
        </CardContent>
      </Card>
    </div>
  );
}

export default HomePage;
