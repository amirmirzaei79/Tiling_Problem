#include <vector>
#include <iostream>

using namespace std;

struct tile
{
    unsigned int left, up;
    char direction;
    /*
    *   0 ->  *   1 -> *   2 -> **   3 -> ** 
    *        **        **       *          *
    */
};

vector <tile> tiles;

bool solve(unsigned int n, unsigned int mx, unsigned int my, 
           unsigned int left = 0, unsigned int up = 0)
{
    if (n < 2 || mx - left >= n || mx < left || my - up >= n || my < up)
        return false;

    if (n == 2)
    {
        tile t;
        t.left = left;
        t.up = up;

        if (mx == left && my == up)
            t.direction = 0;
        else if (mx == left + 1 && my == up)
            t.direction = 1;
        else if (mx == left + 1 && my == up + 1)
            t.direction = 2;
        else if (mx == left && my == up + 1)
            t.direction = 3;
        else
            return false;

        tiles.push_back(t);
        return true;
    }
    else
    {
        bool f = true;
        tile t;
        t.left = left + n / 2 - 1;
        t.up = up + n / 2 - 1;
        if ((mx - left) < n / 2 && (my - up) < n / 2)
        {
            //0
            t.direction = 0;
            f = f & solve(n / 2, mx, my, left, up);
            f = f & solve(n / 2, left + n / 2, up + n / 2 - 1, left + n / 2, up);
            f = f & solve(n / 2, left + n / 2, up + n / 2, left + n / 2, up + n / 2);
            f = f & solve(n / 2, left + n / 2 - 1, up + n / 2, left, up + n / 2);
        }
        else if ((mx - left) >= n / 2 && (my - up) < n / 2)
        {
            //1
            t.direction = 1;
            f = f & solve(n / 2, left + n / 2 - 1, up + n / 2 - 1, left, up);
            f = f & solve(n / 2, mx, my, left + n / 2, up);
            f = f & solve(n / 2, left + n / 2, up + n / 2, left + n / 2, up + n / 2);
            f = f & solve(n / 2, left + n / 2 - 1, up + n / 2, left, up + n / 2);
        }
        else if ((mx - left) >= n / 2 && (my - up) >= n / 2)
        {
            //2
            t.direction = 2;
            f = f & solve(n / 2, left + n / 2 - 1, up + n / 2 - 1, left, up);
            f = f & solve(n / 2, left + n / 2, up + n / 2 - 1, left + n / 2, up);
            f = f & solve(n / 2, mx, my, left + n / 2, up + n / 2);
            f = f & solve(n / 2, left + n / 2 - 1, up + n / 2, left, up + n / 2);
        }
        else // (mx - left) < n / 2 && (my - up) >= n / 2
        {
            //3
            t.direction = 3;
            f = f & solve(n / 2, left + n / 2 - 1, up + n / 2 - 1, left, up);
            f = f & solve(n / 2, left + n / 2, up + n / 2 - 1, left + n / 2, up);
            f = f & solve(n / 2, left + n / 2, up + n / 2, left + n / 2, up + n / 2);
            f = f & solve(n / 2, mx, my, left, up + n / 2);
        }

        tiles.push_back(t);
        return f;
    }
}

int main()
{
    unsigned int n, mx, my;
    cin >> n >> mx >> my;

    solve(n, mx, my);
    for(int i = 0; i < tiles.size(); ++i)
        cout << tiles[i].left << " - " << tiles[i].up << " - " << (int)tiles[i].direction << endl;

    return 0;
}